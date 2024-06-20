package com.ankush.cleancity.Query;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class NamedParameter<T> {
    String key;
    T val;
}
