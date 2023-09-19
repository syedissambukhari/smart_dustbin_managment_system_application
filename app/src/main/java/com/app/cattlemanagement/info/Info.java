package com.app.cattlemanagement.info;

public interface Info {
    String TAG = "bruteforce";

    String TYPE_ADMIN = "Admin";
    String TYPE_CONSUMER = "Consumer";
    String TYPE_DRIVER = "Driver";

    String STATUS_PENDING = "Pending";
    String STATUS_APPROVED = "Approved";
    String STATUS_REJECTED = "Rejected";

    int RV_USER_REGISTRATIONS = 1;
    int RV_TRASH_SOURCES = 2;
    int RV_DRIVER_ASSIGNMENTS = 3;
    int RV_ASSIGNED_DRIVER = 4;

    String USER_IN_ACTIVE = "In-Active";
    String USER_ACTIVE = "Active";
    String USER_REJECT = "Rejected";

    String NODE_USERS = "Users";
    String NODE_TRASH_SOURCE = "TrashSource";
    String NODE_NEIGHBOURHOOD = "Neighbourhood";
}
