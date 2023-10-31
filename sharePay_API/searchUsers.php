<?php

error_reporting(E_ALL);
ini_set('display_errors', 1);

// Include database connection and configuration
include('db.php');

// Check if the HTTP request method is GET
if ($_SERVER['REQUEST_METHOD'] == 'GET') {
    // Retrieve and validate the phone number from the URL
    if (isset($_GET['phone'])) {
        $phone = "%" . $_GET['phone'] . "%";

        // Search for users in the database based on the phone number
        $searchQuery = "SELECT * FROM users WHERE last_name LIKE ? OR first_name LIKE ?";

        // Use prepared statements to prevent SQL injection
        $stmt = $conn->prepare($searchQuery);
        $stmt->bind_param("ss", $phone, $phone );

        if ($stmt->execute()) {
            $result = $stmt->get_result();
            $users = array();

            while ($row = $result->fetch_assoc()) {
                // Customize the data fields as needed
                $user = array(
                    "phone" => $row["phone"],
                    "first_name" => $row["first_name"],
                    "last_name" => $row["last_name"],
                );

                $users[] = $user;
            }

            // Return the search results as JSON
            echo json_encode($users);
        } else {
            // Handle the database error
            http_response_code(500);
            echo json_encode(array("error" => "Failed to search for users."));
        }
    } else {
        // Phone parameter is empty or not provided
        http_response_code(400); // Bad Request
        echo json_encode(array("error" => "Phone parameter is empty or missing."));
    }
} else {
    // Invalid request method
    http_response_code(405);
    echo json_encode(array("error" => "Invalid request method."));
}
?>
