<?php
error_reporting(E_ALL);
ini_set('display_errors', 1);

// Include database connection and configuration
include('db.php');

// Check if the HTTP request method is GET
if ($_SERVER['REQUEST_METHOD'] == 'GET') {
    // Retrieve and validate the user ID from the request
    if (isset($_GET['user_id'])) {
        $user_id = $_GET['user_id'];

        // Search for group members in the database based on the user ID
        $searchQuery = "SELECT g.group_id, g.group_name, g.group_icon, g.admin_id, g.desc, g.bill, g.target_fee,
       g.status, g.recent_transaction, g.date_created, g.date_updated,
       gm.user_name, gm.user_id, gm.collection_share
        FROM `groups` g
        INNER JOIN group_members gm ON g.group_id = gm.group_id
        WHERE gm.user_id = ? AND gm.status != 'pending'
        ORDER BY g.date_updated DESC";

        // Use prepared statements to prevent SQL injection
        $stmt = $conn->prepare($searchQuery);

        if ($stmt) {
            $stmt->bind_param("s", $user_id);
           
            if ($stmt->execute()) {
                $result = $stmt->get_result();
                $users = array();

                while ($row = $result->fetch_assoc()) {
                    // Customize the data fields as needed
                    $user = array(
                        "group_name" => $row["group_name"],
                        "group_id" => $row["group_id"],
                        "recent_transaction" => $row["recent_transaction"],
                        "date_updated" => $row["date_updated"],
                        "admin_id" => $row["admin_id"],
                        "target_fee" => $row["target_fee"],
                        "bill" => $row["bill"],
                        "collection_share" => $row["collection_share"],
                        "status" => $row["status"],
                    );

                    $users[] = $user;
                }

                // Return the search results as JSON
                echo json_encode($users);
            } else {
                // Handle the database error
                http_response_code(500);
                echo json_encode(array("error" => "Failed to fetch groups."));
            }
        } else {
            // Handle the prepare statement error
            http_response_code(500);
            echo json_encode(array("error" => "Failed to prepare the statement: " . $conn->error));
        }
    } else {
        // User ID parameter is missing
        http_response_code(400);
        echo json_encode(array("error" => "User ID parameter is missing."));
    }
} else {
    // Invalid request method
    http_response_code(405);
    echo json_encode(array("error" => "Invalid request method."));
}
