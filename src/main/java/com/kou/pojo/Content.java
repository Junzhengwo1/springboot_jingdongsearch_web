package com.kou.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author JIAJUN KOU
 *
 * 将爬去下来的数据封装成一个对象 内容实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Content {

    private String title;
    private String img;
    private String price;

}
