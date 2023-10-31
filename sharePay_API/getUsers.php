<?php
// Include database connection and configuration
include('db.php');

// Check if the HTTP request method is GET
if ($_SERVER['REQUEST_METHOD'] == 'GET') {
    // Query to fetch all users from the "users" table
    $query = "SELECT * FROM users";

    $result = $conn->query($query);

    if ($result->num_rows > 0) {
        $users = array();

        while ($row = $result->fetch_assoc()) {
            // Build a user object
            $user = array(
                "user_id" => $row["user_id"],
                "phone" => $row["phone"],
                "first_name" => $row["first_name"],
                "last_name" => $row["last_name"],
                "photo" => $row["photo"],
                "date_reg" => $row["date_reg"]
            );

            // Add the user to the users array
            $users[] = $user;
        }

        // Return the list of users as JSON
        echo json_encode($users);
    } else {
        // No users found
        echo json_encode(array("message" => "No users found."));
    }
} else {
    // Invalid request method
    http_response_code(405);
    echo json_encode(array("error" => "Invalid request method."));
}
