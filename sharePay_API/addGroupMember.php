<?php
header('Content-Type: application/json');
// error_reporting(E_ALL);
// ini_set('display_errors', 1);
// Include database connection and configuration
include('db.php');

// Check if the HTTP request method is POST
if ($_SERVER['REQUEST_METHOD'] == 'POST') {
   
    // Retrieve and validate the form parameters
    $group_id = $_POST['group_id'];
    $user_name = $_POST['user_name'];
    $user_id = $_POST['user_id'];
    $collection_share = $_POST['collection_share'];
    $status = "pending";

    // Check if a record with the same user_id already exists
    $checkQuery = "SELECT user_id FROM group_members WHERE user_id = ? AND group_id = ? ";
    $stmtCheck = $conn->prepare($checkQuery);

    if (!$stmtCheck) {
        // Handle the prepare error, e.g., log or display an error message
        echo "Prepare failed: " . $conn->error;
    } else {
        $stmtCheck->bind_param("ii", $user_id, $group_id);
        $stmtCheck->execute();
        $stmtCheck->store_result();

        // If a record with the same user_id exists, do not insert a duplicate
        if ($stmtCheck->num_rows > 0) {
            $response = array('message' => 'Duplicate entry. User already exists in the group.');
            echo json_encode($response);
        } else {
            // No duplicate found, proceed to insert
            $insertQuery = "INSERT INTO group_members (group_id, user_name, user_id, collection_share, status) VALUES (?, ?, ?, ?, ?)";
            $stmt = $conn->prepare($insertQuery);

            if (!$stmt) {
                // Handle the prepare error, e.g., log or display an error message
                echo "Prepare failed: " . $conn->error;
            } else {
                // Continue with binding and executing the prepared statement
                $stmt->bind_param("issds", $group_id, $user_name, $user_id, $collection_share, $status);
                if ($stmt->execute()) {
                    // Return a success message in the response
                    $response = array('message' => 'Group member added successfully');
                    echo json_encode($response);
                } else {
                    // Handle the execution error
                    echo "Execute failed: " . $stmt->error;
                }
            }
        }

        $stmtCheck->close(); // Close the statement
    }
}
?>
