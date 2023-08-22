package com.shopping.admin.user;

import com.shopping.admin.paging.PagingAndSortingHelper;
import com.shopping.library.entity.Role;
import com.shopping.library.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class UserService {

    public static final int USERS_PER_PAGE = 4;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> getAll() {
        return userRepository.findAll(Sort.by("firstName").ascending());
    }

    public void listByPage(int pageNum, PagingAndSortingHelper helper) {
        helper.listEntities(pageNum, USERS_PER_PAGE, userRepository);
    }

    public User getByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<Role> getRoles(){
        return roleRepository.findAll();
    }

    public User save(User user) {
        boolean isEditingMode = user.getId() != null;
        if (isEditingMode) {
            User existingUser = userRepository.findById(user.getId()).get();
            if (user.getPassword().isEmpty()) {
                user.setPassword(existingUser.getPassword());
            } else {
                encodePassword(user);
            }
        } else {
            encodePassword(user);
        }
        return userRepository.save(user);
    }

    public User updateAccount(User userInForm) {
       User userInDB =  userRepository.findById(userInForm.getId()).get();

       if (!userInForm.getPassword().isEmpty()) {
           userInDB.setPassword(userInForm.getPassword());
           encodePassword(userInDB);
       }

       if (userInForm.getPhotos() != null) {
           userInDB.setPhotos(userInForm.getPhotos());
       }

       userInDB.setFirstName(userInForm.getFirstName());
       userInDB.setLastName(userInForm.getLastName());
       return userRepository.save(userInDB);
    }

    private void encodePassword(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
    }

    public boolean isEmailUnique(Integer id, String email) {
        User user = userRepository.findByEmail(email);

        if (id == null) {
            return user == null;
        }

        return user == null || user.getId().equals(id);
    }


    public User get(Integer id) throws UserNotFoundException {
        return userRepository.findById(id).orElseThrow((()->new UserNotFoundException("Could not find any user with id: " + id)));
    }

    public void delete(Integer id) throws UserNotFoundException{
        Long usersCount = userRepository.countById(id);
        if (usersCount == null || usersCount == 0)
            throw new UserNotFoundException("Could not find any user with id "+ id);

        userRepository.deleteById(id);
    }

    public void updateUserEnabledStatus(Integer id, boolean enabled) {
        userRepository.updateEnabledStatus(id, enabled);
    }

}
