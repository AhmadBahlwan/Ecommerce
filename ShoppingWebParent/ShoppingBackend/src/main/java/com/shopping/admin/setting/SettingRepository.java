package com.shopping.admin.setting;

import com.shopping.library.entity.setting.Setting;
import com.shopping.library.entity.setting.SettingCategory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SettingRepository extends CrudRepository<Setting, String> {
    List<Setting> findByCategory(SettingCategory category);
}
