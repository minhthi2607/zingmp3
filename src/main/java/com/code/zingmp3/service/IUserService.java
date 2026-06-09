package com.code.zingmp3.service;

import com.code.zingmp3.model.User;

public interface IUserService extends IGenerateService<User> {
    Boolean login(User user);
}
