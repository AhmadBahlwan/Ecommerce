package com.shopping.admin.user;

import com.shopping.admin.FileUploadUtil;
import com.shopping.library.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public String getFirstPage(Model model) {
        return getByPage(1, model, "firstName", "asc", null);
    }

    @GetMapping("/users/page/{pageNumber}")
    public String getByPage(@PathVariable int pageNumber, Model model, @Param("sortField") String sortField,
                            @Param("sortDirection") String sortDirection, @Param("keyword") String keyword) {
        Page<User> page = userService.getByPage(pageNumber, sortField, sortDirection, keyword);
        List<User> users = page.getContent();

        long startCount = (long) (pageNumber - 1) * UserService.USERS_PER_PAGE + 1;
        long endCount = startCount + UserService.USERS_PER_PAGE - 1;
        if (endCount > page.getTotalElements())
            endCount = page.getTotalElements();

        String reversedSortDir = sortDirection.equals("asc") ? "desc" : "asc";

        model.addAttribute("users", users);
        model.addAttribute("totalElements", page.getTotalElements());
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("sortField", sortField);
        model.addAttribute("reversedSortDir", reversedSortDir);
        model.addAttribute("keyword", keyword);


        return "users";
    }

    @GetMapping("/users/new")
    public String createUser(Model model) {
        User user = new User();
        user.setEnabled(true);
        model.addAttribute("user", user);
        model.addAttribute("roles", userService.getRoles());
        model.addAttribute("pageTitle", "Create New User");
        return "user_form";
    }

    @PostMapping("/users/save")
    public String saveUser(User user, RedirectAttributes redirectAttributes, @RequestParam MultipartFile image) throws IOException {
        if (!image.isEmpty()) {
            String fileName = StringUtils.cleanPath(image.getOriginalFilename());
            user.setPhotos(fileName);
            User savedUser = userService.save(user);
            String uploadDir = "user-photos/" + savedUser.getId();
            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, image);
        } else {
            if (user.getPhotos().isEmpty()) user.setPhotos(null);
            userService.save(user);
        }

        redirectAttributes.addFlashAttribute("message", "The user has been saved successfully.");
        return "redirect:/users";
    }

    @GetMapping("/users/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        try{
            User user = userService.get(id);
            model.addAttribute("user", user);
            model.addAttribute("pageTitle", "Edit User (ID: " + user.getId() + ")");
            model.addAttribute("roles", userService.getRoles());
            return "user_form";
        }catch (UserNotFoundException ex){
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
            return "redirect:/users";
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
        return "redirect:/users";
    }

    @GetMapping("/users/{id}/enabled/{enabled}")
    public String updateUserEnabledStatus(@PathVariable Integer id,
                                          @PathVariable boolean enabled,
                                          RedirectAttributes redirectAttributes) {

        userService.updateUserEnabledStatus(id, enabled);
        String status =  enabled ? "enabled" : "disabled";
        String message = "The user ID " + id + " has been " + status + " successfully";
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/users";

    }
}
