<?php
// Connect to the database (replace with your actual database credentials)
$connection = new mysqli("localhost", "username", "password", "your_database");

if ($connection->connect_error) {
    die("Connection failed: " . $connection->connect_error);
}

// Create User
function createUser($phone, $first_name, $last_name, $photo) {
    global $connection;
    $query = "INSERT INTO Users (phone, first_name, last_name, photo) VALUES (?, ?, ?, ?)";
    $stmt = $connection->prepare($query);
    $stmt->bind_param("ssss", $phone, $first_name, $last_name, $photo);
    
    if ($stmt->execute()) {
        return "User created successfully";
    } else {
        return "User creation failed";
    }
}

// Read User
function readUser($user_id) {
    global $connection;
    $query = "SELECT * FROM Users WHERE user_id = ?";
    $stmt = $connection->prepare($query);
    $stmt->bind_param("i", $user_id);
    $stmt->execute();
    $result = $stmt->get_result();
    
    if ($row = $result->fetch_assoc()) {
        return $row;
    } else {
        return null;
    }
}

// Update User
function updateUser($user_id, $phone, $first_name, $last_name, $photo) {
    global $connection;
    $query = "UPDATE Users SET phone=?, first_name=?, last_name=?, photo=? WHERE user_id=?";
    $stmt = $connection->prepare($query);
    $stmt->bind_param("ssssi", $phone, $first_name, $last_name, $photo, $user_id);
    
    if ($stmt->execute()) {
        return "User updated successfully";
    } else {
        return "User update failed";
    }
}

// Delete User
function deleteUser($user_id) {
    global $connection;
    $query = "DELETE FROM Users WHERE user_id=?";
    $stmt = $connection->prepare($query);
    $stmt->bind_param("i", $user_id);
    
    if ($stmt->execute()) {
        return "User deleted successfully";
    } else {
        return "User deletion failed";
    }
}

// Create Group
function createGroup($group_name, $admin_id, $desc, $target_fee) {
    global $connection;
    $query = "INSERT INTO Groups (group_name, admin_id, `desc`, target_fee) VALUES (?, ?, ?, ?)";
    $stmt = $connection->prepare($query);
    $stmt->bind_param("siss", $group_name, $admin_id, $desc, $target_fee);
    
    if ($stmt->execute()) {
        return "Group created successfully";
    } else {
        return "Group creation failed";
    }
}

// Read Group
function readGroup($group_id) {
    global $connection;
    $query = "SELECT * FROM Groups WHERE group_id = ?";
    $stmt = $connection->prepare($query);
    $stmt->bind_param("i", $group_id);
    $stmt->execute();
    $result = $stmt->get_result();
    
    if ($row = $result->fetch_assoc()) {
        return $row;
    } else {
        return null;
    }
}

// Update Group
function updateGroup($group_id, $group_name, $desc, $target_fee) {
    global $connection;
    $query = "UPDATE Groups SET group_name=?, `desc`=?, target_fee=? WHERE group_id=?";
    $stmt = $connection->prepare($query);
    $stmt->bind_param("sssi", $group_name, $desc, $target_fee, $group_id);
    
    if ($stmt->execute()) {
        return "Group updated successfully";
    } else {
        return "Group update failed";
    }
}

// Delete Group
function deleteGroup($group_id) {
    global $connection;
    $query = "DELETE FROM Groups WHERE group_id=?";
    $stmt = $connection->prepare($query);
    $stmt->bind_param("i", $group_id);
    
    if ($stmt->execute()) {
        return "Group deleted successfully";
    } else {
        return "Group deletion failed";
    }
}

// Create Group Member
function createGroupMember($group_id, $user_id, $collection_share) {
    global $connection;
    $query = "INSERT INTO Group_Members (group_id, user_id, collection_share) VALUES (?, ?, ?)";
    $stmt = $connection->prepare($query);
    $stmt->bind_param("idd", $group_id, $user_id, $collection_share);
    
    if ($stmt->execute()) {
        return "Group member added successfully";
    } else {
        return "Group member addition failed";
    }
}

// Read Group Member
function readGroupMember($member_id) {
    global $connection;
    $query = "SELECT * FROM Group_Members WHERE id = ?";
    $stmt = $connection->prepare($query);
    $stmt->bind_param("i", $member_id);
    $stmt->execute();
    $result = $stmt->get_result();
    
    if ($row = $result->fetch_assoc()) {
        return $row;
    } else {
        return null;
    }
}

// Update Group Member
function updateGroupMember($member_id, $collection_share) {
    global $connection;
    $query = "UPDATE Group_Members SET collection_share=? WHERE id=?";
    $stmt = $connection->prepare($query);
    $stmt->bind_param("di", $collection_share, $member_id);
    
    if ($stmt->execute()) {
        return "Group member updated successfully";
    } else {
        return "Group member update failed";
    }
}

// Delete Group Member
function deleteGroupMember($member_id) {
    global $connection;
    $query = "DELETE FROM Group_Members WHERE id=?";
    $stmt = $connection->prepare($query);
    $stmt->bind_param("i", $member_id);
    
    if ($stmt->execute()) {
        return "Group member deleted successfully";
    } else {
        return "Group member deletion failed";
    }
}

// Create Message
function createMessage($user_id, $amount_deposited, $balance) {
    global $connection;
    $query = "INSERT INTO Messages (user_id, amount_deposited, balance) VALUES (?, ?, ?)";
    $stmt = $connection->prepare($query);
    $stmt->bind_param("idd", $user_id, $amount_deposited, $balance);
    
    if ($stmt->execute()) {
        return "Message created successfully";
    } else {
        return "Message creation failed";
    }
}

// Read Message
function readMessage($message_id) {
    global $connection;
    $query = "SELECT * FROM Messages WHERE id = ?";
    $stmt = $connection->prepare($query);
    $stmt->bind_param("i", $message_id);
    $stmt->execute();
    $result = $stmt->get_result();
    
    if ($row = $result->fetch_assoc()) {
        return $row;
    } else {
        return null;
    }
}

// Update Message
function updateMessage($message_id, $amount_deposited, $balance) {
    global $connection;
    $query = "UPDATE Messages SET amount_deposited=?, balance=? WHERE id=?";
    $stmt = $connection->prepare($query);
    $stmt->bind_param("ddi", $amount_deposited, $balance, $message_id);
    
    if ($stmt->execute()) {
        return "Message updated successfully";
    } else {
        return "Message update failed";
    }
}

// Delete Message
function deleteMessage($message_id) {
    global $connection;
    $query = "DELETE FROM Messages WHERE id=?";
    $stmt = $connection->prepare($query);
    $stmt->bind_param("i", $message_id);
    
    if ($stmt->execute()) {
        return "Message deleted successfully";
    } else {
        return "Message deletion failed";
    }
}

// Create Payment History
function createPaymentHistory($group_id, $narration, $fee) {
    global $connection;
    $query = "INSERT INTO Payment_History (group_id, narration, fee) VALUES (?, ?, ?)";
    $stmt = $connection->prepare($query);
    $stmt->bind_param("isd", $group_id, $narration, $fee);
    
    if ($stmt->execute()) {
        return "Payment history created successfully";
    } else {
        return "Payment history creation failed";
    }
}

// Read Payment History
function readPaymentHistory($payment_id) {
    global $connection;
    $query = "SELECT * FROM Payment_History WHERE id = ?";
    $stmt = $connection->prepare($query);
    $stmt->bind_param("i", $payment_id);
    $stmt->execute();
    $result = $stmt->get_result();
    
    if ($row = $result->fetch_assoc()) {
        return $row;
    } else {
        return null;
    }
}

// Update Payment History
function updatePaymentHistory($payment_id, $narration, $fee) {
    global $connection;
    $query = "UPDATE Payment_History SET narration=?, fee=? WHERE id=?";
    $stmt = $connection->prepare($query);
    $stmt->bind_param("sdi", $narration, $fee, $payment_id);
    
    if ($stmt->execute()) {
        return "Payment history updated successfully";
    } else {
        return "Payment history update failed";
    }
}

// Delete Payment History
function deletePaymentHistory($payment_id) {
    global $connection;
    $query = "DELETE FROM Payment_History WHERE id=?";
    $stmt = $connection->prepare($query);
    $stmt->bind_param("i", $payment_id);
    
    if ($stmt->execute()) {
        return "Payment history deleted successfully";
    } else {
        return "Payment history deletion failed";
    }
}

// Close the database connection
$connection->close();
