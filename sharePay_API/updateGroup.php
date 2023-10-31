<?php
error_reporting(E_ALL);
ini_set('display_errors', 1);
// Include database connection and configuration
include('db.php');

// Check if the HTTP request method is POST
if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    // Check if the required fields are present in the POST parameters
    if (isset($_POST['group_id'], $_POST['status'])) {
        // Sanitize and validate the data (you should perform more thorough validation)
        $groupID = $_POST['group_id'];
        $status = $_POST['status'];
        $recent_transaction = $_POST['recent_transaction'];

        // Perform SQL update to change the status of the specified group
        $query = "UPDATE groups SET status = ?, recent_transaction = ? WHERE group_id = ?";
        $stmt = $conn->prepare($query);

        if (!$stmt) {
            // Handle the prepare error, e.g., log or display an error message
            echo "Prepare failed: " . $conn->error;
        } else {
            // Continue with binding and executing the prepared statement
            $stmt->bind_param("sss", $status, $recent_transaction, $groupID);
            if ($stmt->execute()) {
                // The statement executed successfully
                // Return a success message in the response
                $response = array('message' => 'Group status updated successfully');
                echo json_encode($response);
            } else {
                // Handle the execution error
                echo "Execute failed: " . $stmt->error;
            }
        }

        // Close database connection
        $stmt->close();
    } else {
        // Handle missing or invalid data in the request
        $response = array('error' => 'Invalid or missing group_id or new_status');
        echo json_encode($response);
    }
}
?>

