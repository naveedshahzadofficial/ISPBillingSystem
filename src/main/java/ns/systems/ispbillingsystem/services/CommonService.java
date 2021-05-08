/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ns.systems.ispbillingsystem.services;

import java.util.List;

/**
 *
 * @author naveed
 */
public interface CommonService<T> {
    List<T> list();
    void save(T object);
    void update(T object);
    T find(Long Id);
    public T findByKey(String ColumnName, String ColumnValue);
    public List<T> getWhere(String ColumnName, String ColumnValue);
    public List<T> whereLike(String ColumnName, String ColumnValue,String JoinType);
    void delete(T object);
}
