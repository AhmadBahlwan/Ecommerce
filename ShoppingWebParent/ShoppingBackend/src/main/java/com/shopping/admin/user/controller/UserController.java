package com.shopping.admin.user.controller;

import com.shopping.admin.FileUploadUtil;
import com.shopping.admin.paging.PagingAndSortingHelper;
import com.shopping.admin.paging.PagingAndSortingParam;
import com.shopping.admin.user.UserNotFoundException;
import com.shopping.admin.user.UserService;
import com.shopping.admin.user.export.UserCsvExporter;
import com.shopping.admin.user.export.UserExcelExporter;
import com.shopping.admin.user.export.UserPdfExporter;
import com.shopping.library.entity.User;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
public class UserController {

    private String defaultRedirectURL = "redirect:/users/page/1?sortField=firstName&sortDirection=asc";

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public String listFirstPage(Model model) {
        return defaultRedirectURL;
    }

    @GetMapping("/users/page/{pageNum}")
    public String listByPage(@PagingAndSortingParam(listName = "listUsers", moduleURL = "/users") PagingAndSortingHelper helper,
                             @PathVariable(name = "pageNum") int pageNum) {
        userService.listByPage(pageNum, helper);
        return "users/users";
    }

    @GetMapping("/users/new")
    public String createUser(Model model) {
        User user = new User();
        user.setEnabled(true);
        model.addAttribute("user", user);
        model.addAttribute("roles", userService.getRoles());
        model.addAttribute("pageTitle", "Create New User");
        return "users/user_form";
    }

    @PostMapping("/users/save")
    public String saveUser(User user, RedirectAttributes redirectAttributes, @RequestParam MultipartFile image) throws IOException {
        if (!image.isEmpty()) {
            String fileName = StringUtils.cleanPath(image.getOriginalFilename());
            user.setPhotos(fileName);
            User savedUser = userService.save(user);
            String uploadDir = "user-photos/" + savedUser.getId();
            FileUploadUtil.cleanDirectory(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, image);
        } else {
            if (user.getPhotos().isEmpty()) user.setPhotos(null);
            userService.save(user);
        }

        redirectAttributes.addFlashAttribute("message", "The user has been saved successfully.");

        String firstPartOfEmail = user.getEmail().split("@")[0];
        return "redirect:/users/page/1?sortField=id&sortDirection=asc&keyword=" + firstPartOfEmail;
    }

    @GetMapping("/users/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        try{
            User user = userService.get(id);
            model.addAttribute("user", user);
            model.addAttribute("pageTitle", "Edit User (ID: " + user.getId() + ")");
            model.addAttribute("roles", userService.getRoles());
            return "users/user_form";
        }catch (UserNotFoundException ex){
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
            return defaultRedirectURL;
        }

    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        try{
            userService.delete(id);
            redirectAttributes.addFlashAttribute("message", "The user with ID "
                    + id + "has been deleted successfully");
        }catch (UserNotFoundException ex){
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
        }
        return defaultRedirectURL;
    }

    @GetMapping("/users/{id}/enabled/{enabled}")
    public String updateUserEnabledStatus(@PathVariable Integer id,
                                          @PathVariable boolean enabled,
                                          RedirectAttributes redirectAttributes) {

        userService.updateUserEnabledStatus(id, enabled);
        String status =  enabled ? "enabled" : "disabled";
        String message = "The user ID " + id + " has been " + status + " successfully";
        redirectAttributes.addFlashAttribute("message", message);
        return defaultRedirectURL;

    }

    @GetMapping("/users/export/csv")
    public void exportToCSV(HttpServletResponse response) throws IOException {
        UserCsvExporter csvExporter = new UserCsvExporter();
        csvExporter.export(userService.getAll(), response);
    }

    @GetMapping("/users/export/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        UserExcelExporter excelExporter = new UserExcelExporter();
        excelExporter.export(userService.getAll(), response);
    }

    @GetMapping("/users/export/pdf")
    public void exportToPdf(HttpServletResponse response) throws IOException {
        UserPdfExporter pdfExporter = new UserPdfExporter();
        pdfExporter.export(userService.getAll(), response);
    }
}
