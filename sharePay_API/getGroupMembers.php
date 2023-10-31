<?php
error_reporting(E_ALL);
ini_set('display_errors', 1);

// Include database connection and configuration
include('db.php');

// Check if the HTTP request method is GET
if ($_SERVER['REQUEST_METHOD'] == 'GET') {
    // Retrieve and validate the group ID from the request
    if (isset($_GET['group_id'])) {
        $groupId = $_GET['group_id'];

        // Search for group members in the database based on the group ID
        $searchQuery = "SELECT user_name, user_id, collection_share FROM group_members WHERE group_id = ?";

        // Use prepared statements to prevent SQL injection
        $stmt = $conn->prepare($searchQuery);
        $stmt->bind_param("s", $groupId);

        if ($stmt->execute()) {
            $result = $stmt->get_result();
            $users = array();

            while ($row = $result->fetch_assoc()) {
                // Customize the data fields as needed
                $user = array(
                    "user_name" => $row["user_name"],
                    "user_id" => $row["user_id"],
                    "collection_share" => $row["collection_share"],
                );

                $users[] = $user;
            }

            // Return the search results as JSON
            echo json_encode($users);
        } else {
            // Handle the database error
            http_response_code(500);
            echo json_encode(array("error" => "Failed to fetch group members."));
        }
    } else {
        // Group ID parameter is missing
        http_response_code(400);
        echo json_encode(array("error" => "Group ID parameter is missing."));
    }
} else {
    // Invalid request method
    http_response_code(405);
    echo json_encode(array("error" => "Invalid request method."));
}
