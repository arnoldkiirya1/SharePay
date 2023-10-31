<?php
error_reporting(E_ALL);
ini_set('display_errors', 1);
// Include database connection and configuration
include('db.php');

// Check if the HTTP request method is POST
if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    // Get the data from the request body
    $data = json_decode(file_get_contents("php://input"));

    // Extract and validate group data
    $groupName = isset($data->group_name) ? $data->group_name : null;
    $groupIcon = isset($data->group_icon) ? $data->group_icon : null;
    $adminId = isset($data->admin_id) ? $data->admin_id : null;
    $description = isset($data->desc) ? $data->desc : null;
    $bill = isset($data->bill) ? $data->bill : null;
    $targetFee = isset($data->target_fee) ? $data->target_fee : null;
    $status = "active";
    $recentTransaction = isset($data->recent_transaction) ? $data->recent_transaction : null;

     // Perform SQL insert for creating a new group
        $query = "INSERT INTO groups (group_name, group_icon, admin_id, `desc`, bill, target_fee, status, recent_transaction, date_created) VALUES (?, ?, ?, ?, ?, ?, ?, ?, NOW())";
        $stmt = $conn->prepare($query);
       
        if (!$stmt) {
            // Handle the prepare error, e.g., log or display an error message
            echo "Prepare failed: " . $conn->error;
        } else {
            // Continue with binding and executing the prepared statement
           $stmt->bind_param("ssssssss", $groupName, $groupIcon, $adminId, $description, $bill, $targetFee, $status, $recentTransaction);
            if ($stmt->execute()) {
                // The statement executed successfully
                // Get the ID of the last inserted record
                $groupID = $stmt->insert_id;
        
                // Return the group ID in the response
                $response = array('message' => 'Group created successfully', 'group_id' => $groupID);
        echo json_encode($response);
            } else {
                // Handle the execution error
                echo "Execute failed: " . $stmt->error;
            }
        }

        // Close database connection
        $stmt->close();
    }

?>
