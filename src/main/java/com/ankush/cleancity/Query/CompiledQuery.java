package com.ankush.cleancity.Query;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class CompiledQuery {
    private String base;
    private Object[] values;

}
