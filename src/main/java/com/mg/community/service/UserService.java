package com.mg.community.service;

import com.mg.community.model.User;

public interface UserService {

    /**
     * 通过外部网站的account id查询用户信息；
     * @param accountId
     * @return
     */
    public User findByAccountId(String accountId);

    /**
     * 通过token获取User
     * @param token
     * @return
     */
    public User findByToken(String token);

    /**
     * 通过ID获取User
     * @param id
     * @return
     */
    public User findById(Integer id);
    /**
     * 新增或修改User
     * @param user
     */
    void createOrUpdate(User user);
}
