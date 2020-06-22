package com.lijincan.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: lijincan
 * @date: 2020年02月26日 16:13
 * @Description: TODO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private String uid;
    private String username;
    private String password;
    private String email;
    private int type;
    private String job;
    private String sex;
    private String star;
    private String name;
}
