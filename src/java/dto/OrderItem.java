/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ptd
 */
public class OrderItem implements Serializable{
    private List<OrderDetail> list;

    public OrderItem() {
        list = new ArrayList<>();       
    }

    public OrderItem(List<OrderDetail> list) {
        this.list = list;
    }

    public List<OrderDetail> getList() {
        return list;
    }

    public void setList(List<OrderDetail> list) {
        this.list = list;
    }
    
    public int getLength(){
        return list.size();
    }
    
    public int getOrderId(){
        return list.get(0).getOrderId();
    }
    
    public int getOrderId(int index){
        return list.get(index).getOrderId();
    }
    
    public int getOrderDetailId(){
        return list.get(0).getOrderDetailId();
    }
    
    public int getItemTypeId(int index){
        return list.get(index).getItemId();
    }
    
    public int getOrderDetailId(int index){
        return list.get(index).getOrderDetailId();
    }
    
    public String getOrderDetailItemType(int index){
        return list.get(index).getItemType();
    }
    
}
