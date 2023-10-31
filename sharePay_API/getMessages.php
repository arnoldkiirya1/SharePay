<?php

error_reporting(E_ALL);
ini_set('display_errors', 1);

// Check if the request method is GET
if ($_SERVER['REQUEST_METHOD'] === 'GET') {
    // Retrieve group_id from the query string
    $group_id = isset($_GET['group_id']) ? $_GET['group_id'] : null;

    if ($group_id === null) {
        echo "Group ID is required.";
    } else {
        // Retrieve messages from your database filtered by group_id (use your database connection and query here)

        // Example using MySQLi (you should use prepared statements for security)
        $mysqli = new mysqli("localhost", "root", "nJHttms84WLa", "sharepay");
        if ($mysqli->connect_error) {
            die("Connection failed: " . $mysqli->connect_error);
        }

        $sql = "SELECT id, user_id, name, group_id, amount_deposited, balance, timestamp FROM messages WHERE group_id = ?";
        $stmt = $mysqli->prepare($sql);

        if ($stmt) {
            $stmt->bind_param("i", $group_id);
            $stmt->execute();
            $result = $stmt->get_result();

            if ($result) {
                $messages = array();

                while ($row = $result->fetch_assoc()) {
                    $messages[] = $row;
                }

                echo json_encode($messages);
            } else {
                echo "Error fetching messages: " . $mysqli->error;
            }

            $stmt->close();
        } else {
            echo "Prepare statement failed: " . $mysqli->error;
        }

        $mysqli->close();
    }
} else {
    echo "Invalid request method.";
}
?>
