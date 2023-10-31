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

        // Search for group members with status = "pending" based on the user ID
        $searchQuery = "SELECT gm.group_id, gm.user_id, gm.user_name, gm.collection_share, gm.status, g.group_name, g.group_icon, g.admin_id, g.desc, g.bill, g.target_fee, g.recent_transaction, g.date_created, g.date_updated
        FROM group_members gm
        INNER JOIN groups g ON gm.group_id = g.group_id
        WHERE gm.user_id = ? AND gm.status = 'pending'
        ORDER BY gm.group_id DESC";

        // Use prepared statements to prevent SQL injection
        $stmt = $conn->prepare($searchQuery);

        if ($stmt) {
            $stmt->bind_param("s", $user_id);

            if ($stmt->execute()) {
                $result = $stmt->get_result();
                $pendingGroupMembers = array();

                while ($row = $result->fetch_assoc()) {
                    // Customize the data fields as needed
                    $pendingGroupMember = array(
                        "group_id" => $row["group_id"],
                        "user_id" => $row["user_id"],
                        "user_name" => $row["user_name"],
                        "collection_share" => $row["collection_share"],
                        "status" => $row["status"],
                        "group_name" => $row["group_name"],
                        "group_icon" => $row["group_icon"],
                        "admin_id" => $row["admin_id"],
                        "desc" => $row["desc"],
                        "bill" => $row["bill"],
                        "target_fee" => $row["target_fee"],
                        "recent_transaction" => $row["recent_transaction"],
                        "date_created" => $row["date_created"],
                        "date_updated" => $row["date_updated"]
                    );

                    $pendingGroupMembers[] = $pendingGroupMember;
                }

                // Return the search results as JSON
                echo json_encode($pendingGroupMembers);
            } else {
                // Handle the database error
                http_response_code(500);
                echo json_encode(array("error" => "Failed to fetch pending group members."));
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
