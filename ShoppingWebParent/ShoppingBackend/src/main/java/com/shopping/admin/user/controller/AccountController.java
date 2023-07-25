package com.shopping.admin.user.controller;


import com.shopping.admin.FileUploadUtil;
import com.shopping.admin.security.ShoppingUserDetails;
import com.shopping.admin.user.UserService;
import com.shopping.library.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
public class AccountController {

    @Autowired
    private UserService userService;

    @GetMapping("/account")
    public String viewDetails(@AuthenticationPrincipal ShoppingUserDetails loggedUser,
                              Model model) {
        User user = userService.getByEmail(loggedUser.getUsername());
        model.addAttribute("user", user);

        return "users/account_form";
    }


    @PostMapping("/account/update")
    public String updateDetails(User user, RedirectAttributes redirectAttributes,@AuthenticationPrincipal ShoppingUserDetails loggedUser,
                                @RequestParam MultipartFile image) throws IOException {
        if (!image.isEmpty()) {
            String fileName = StringUtils.cleanPath(image.getOriginalFilename());
            user.setPhotos(fileName);
            User savedUser = userService.updateAccount(user);
            String uploadDir = "user-photos/" + savedUser.getId();
            FileUploadUtil.cleanDirectory(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, image);
        } else {
            if (user.getPhotos().isEmpty()) user.setPhotos(null);
            userService.updateAccount(user);
        }

        loggedUser.setFirstName(user.getFirstName());
        loggedUser.setLastName(user.getLastName());

        redirectAttributes.addFlashAttribute("message", "Your account details has been updated successfully.");

        return "redirect:/account";
    }

}
