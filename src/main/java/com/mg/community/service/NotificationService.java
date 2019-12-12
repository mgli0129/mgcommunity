package com.mg.community.service;

import com.mg.community.dto.NotificationDTO;
import com.mg.community.model.Notification;

import java.util.List;

public interface NotificationService {
    void create(Notification notification);

    List<NotificationDTO> findNotificationByReceiver(Long receiver);

    int countUnread(Long receiver);

    void readNotify(Long receiver);

    Notification findById(Long id);
}
