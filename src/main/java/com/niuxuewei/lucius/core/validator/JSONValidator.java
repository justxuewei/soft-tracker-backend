package com.niuxuewei.lucius.core.validator;

import com.alibaba.fastjson.JSONObject;
import com.niuxuewei.lucius.core.exception.InvalidParamException;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JSONValidator {

    private JSONObject object;

    public String validate(String key) {
        if (object.getString(key) == null) {
            throw new InvalidParamException();
        } else {
            return object.getString(key);
        }
    }

}
