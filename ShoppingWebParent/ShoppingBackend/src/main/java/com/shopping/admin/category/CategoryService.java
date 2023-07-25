package com.shopping.admin.category;

import com.shopping.admin.user.UserNotFoundException;
import com.shopping.library.entity.Category;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Transactional
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAll(String sortDir){
        Sort sort = Sort.by("name");

        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        return listHierarchicalCategories(categoryRepository.findRootCategories(sort), sortDir);
    }

    private List<Category> listHierarchicalCategories(List<Category> rootCategories, String sortDir) {
        List<Category> hierarchicalCategories = new ArrayList<>();

        for (Category root: rootCategories) {
            hierarchicalCategories.add(Category.copyFullCategory(root));

            for (Category subCategory : sortedSubCategories(root.getChildren(),  sortDir)) {
                String name = "--" + subCategory.getName();
                hierarchicalCategories.add(Category.copyFullCategory(subCategory, name));

                listSubHierarchicalCategories(hierarchicalCategories, subCategory, 1, sortDir);
            }
        }
        return hierarchicalCategories;
    }

    private void listSubHierarchicalCategories(List<Category>hierarchicalCategories ,Category parent, int subLevel,
                                               String sortDir) {
        Set<Category> children = sortedSubCategories(parent.getChildren(), sortDir);
        int newSubLevel = subLevel + 1;

        for (Category subCategory: children) {
            StringBuilder name = new StringBuilder();
            for (int i = 0; i < newSubLevel; i++) {
                name.append("--");
            }
            name.append(subCategory.getName());
            hierarchicalCategories.add(Category.copyFullCategory(subCategory, name.toString()));

            listSubHierarchicalCategories(hierarchicalCategories, subCategory, newSubLevel, sortDir);
        }
    }

    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    public List<Category> listCategoriesUsedInForm() {
        List<Category> categoriesUsedInForm = new ArrayList<>();

        for (Category category : categoryRepository.findRootCategories(Sort.by("name").ascending() )) {
            if (category.getParent() == null) {
                categoriesUsedInForm.add(Category.copyIdAndName(category));
                for (Category subCategory : sortedSubCategories(category.getChildren())) {
                    String name = "--" + subCategory.getName();
                    categoriesUsedInForm.add(Category.copyIdAndName(subCategory.getId(), name));

                    listSubCategoriesUsedInForm(categoriesUsedInForm, subCategory, 1);
                }
            }
        }
        return categoriesUsedInForm;
    }

    private void listSubCategoriesUsedInForm(List<Category> categoriesUsedInForm, Category parent, int subLevel) {
        int newSubLevel = subLevel + 1;
        for (Category subCategory: sortedSubCategories(parent.getChildren())) {
            StringBuilder name = new StringBuilder();
            for (int i = 0 ; i< newSubLevel; i++) {
                name.append("--");
            }
            name.append(subCategory.getName());
            categoriesUsedInForm.add(Category.copyIdAndName(Category.copyIdAndName(subCategory.getId(), name.toString())));
            listSubCategoriesUsedInForm(categoriesUsedInForm, subCategory, newSubLevel);
        }
    }

    public Category get(Integer id) throws CategoryNotFoundException {
        try {
            return categoryRepository.findById(id).get();
        }catch (NoSuchElementException ex) {
            throw new CategoryNotFoundException("Could not find any category with ID " + id);
        }
    }

    public String checkUnique(Integer id, String name, String alias) {
        boolean isCreating = (id == null || id == 0);
        Category category = categoryRepository.findByName(name);
        if (isCreating) {
            if (category != null) {
                return "DuplicateName";
            } else {
                category = categoryRepository.findByAlias(alias);
                if (category != null) {
                    return "DuplicateAlias";
                }
            }
        } else {
            if (category != null && !Objects.equals(category.getId(), id)) {
                return "DuplicateName";
            }
            category = categoryRepository.findByAlias(alias);
            if (category != null && !Objects.equals(category.getId(), id)) {
                return "DuplicateAlias";
            }
        }
        return "OK";
    }

    private SortedSet<Category> sortedSubCategories(Set<Category> children) {
        return sortedSubCategories(children, "asc");
    }

    private SortedSet<Category> sortedSubCategories(Set<Category> children, String sortDir) {
        SortedSet<Category> sortedChildren = new TreeSet<>((cat1,cat2)-> sortDir.equals("asc") ?
                cat1.getName().compareTo(cat2.getName())
                : cat2.getName().compareTo(cat1.getName()));

        sortedChildren.addAll(children);
        return sortedChildren;
    }

    public void delete(Integer id) throws CategoryNotFoundException {
        Long usersCount = categoryRepository.countById(id);
        if (usersCount == null || usersCount == 0)
            throw new CategoryNotFoundException("Could not find any category with id "+ id);

        categoryRepository.deleteById(id);
    }

    public void updateUserEnabledStatus(Integer id, boolean enabled) {
        categoryRepository.updateEnabledStatus(id, enabled);
    }
}
