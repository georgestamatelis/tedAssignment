package com.example.demo.Recomendation;

import com.example.demo.appartment;
import lombok.Data;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class UserVector {
    @Id
    private String UserName;
    @ElementCollection
    private
    List<appartment> TopRecomended=new ArrayList<>();

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public List<appartment> getTopRecomended() {
        return TopRecomended;
    }

    public void setTopRecomended(List<appartment> topRecomended) {
        TopRecomended = topRecomended;
    }
}
