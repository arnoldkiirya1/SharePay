<?php
// Include necessary database connection and functions here

// Check if the request method is POST
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    
    // Check if the required fields are present in the POST parameters
    if (isset($_POST['user_id'], $_POST['group_id'], $_POST['amount_deposited'], $_POST['balance'], $_POST['timestamp'])) {
        // Sanitize and validate the data (you should perform more thorough validation)

        $userId = $_POST['user_id'];
        $groupId = $_POST['group_id'];
        $name = $_POST['name'];
        $amountDeposited = $_POST['amount_deposited'];
        $balance = $_POST['balance'];
        $timestamp = $_POST['timestamp'];
       

        // Insert the message data into your database (use your database connection and query here)

        // Example using MySQLi (you should use prepared statements for security)
         $mysqli = new mysqli("localhost", "root", "nJHttms84WLa", "sharepay");
        if ($mysqli->connect_error) {
            die("Connection failed: " . $mysqli->connect_error);
        }

        $sql = "INSERT INTO messages (user_id, name, group_id, amount_deposited, balance, timestamp) VALUES (?, ?, ?, ?, ?, ?)";
        $stmt = $mysqli->prepare($sql);
        $stmt->bind_param("sssdds", $userId, $name, $groupId, $amountDeposited, $balance, $timestamp);

        if ($stmt->execute()) {
            echo "Message created successfully";
        } else {
            echo "Error creating message: " . $stmt->error;
        }

        $stmt->close();
        $mysqli->close();
    } else {
        echo "Incomplete or invalid data.";
    }
} else {
    echo "Invalid request method.";
}
?>
