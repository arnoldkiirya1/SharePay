<?php
header('Content-Type: application/json');

// Include database connection and configuration
include('db.php');

// Check if the HTTP request method is POST
if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    // Retrieve and validate the form parameters
    $group_id = $_POST['group_id'];
    $user_id = $_POST['user_id'];
    $new_status = "active"; 

    // Perform SQL update to change the status of the specified group member
    $query = "UPDATE group_members SET status = ? WHERE group_id = ? AND user_id = ?";
    $stmt = $conn->prepare($query);

    if (!$stmt) {
        // Handle the prepare error, e.g., log or display an error message
        echo "Prepare failed: " . $conn->error;
    } else {
        // Continue with binding and executing the prepared statement
        $stmt->bind_param("sss", $new_status, $group_id, $user_id);
        if ($stmt->execute()) {
            // Return a success message in the response
            $response = array('message' => 'Group member status updated successfully');
            echo json_encode($response);
        } else {
            // Handle the execution error
            echo "Execute failed: " . $stmt->error;
        }
    }
    $stmt->close(); // Close the statement
}
?>
