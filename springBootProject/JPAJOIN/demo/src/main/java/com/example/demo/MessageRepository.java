package com.example.demo;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MessageRepository extends CrudRepository<Message, Integer> {
    List<Message> findAllByReceiverUsn(String ReceiverUsn);
    List<Message> findAllByReceiverUsnAndSenderUsn(String ReceiverUsn,String SenderUsn);
    List<Message> findAllByReceiverUsnAndSenderUsnOrderByDate(String ReceiverUsn,String SenderUsn);
    List<Message> findAllBySender(User s);
    List<Message> findAllByReceiver(User r);
}
