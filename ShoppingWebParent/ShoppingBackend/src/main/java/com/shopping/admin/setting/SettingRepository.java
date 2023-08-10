package com.shopping.admin.setting;

import com.shopping.library.entity.Setting;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SettingRepository extends CrudRepository<Setting, String> {

}
