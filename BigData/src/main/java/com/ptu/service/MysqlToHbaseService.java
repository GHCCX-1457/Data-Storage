package com.ptu.service;

import java.io.IOException;

public interface MysqlToHbaseService {
    boolean importAll(String HbaseTableName) throws IOException;
}
