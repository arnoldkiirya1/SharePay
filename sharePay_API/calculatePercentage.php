<?php
// Include necessary database connection and functions here

// Check if the request method is POST
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    // Retrieve the group_id from the POST data
    $group_id = isset($_POST['group_id']) ? intval($_POST['group_id']) : 0;

    if ($group_id > 0) {
        // Create a MySQLi connection (you should use prepared statements for security)
        $mysqli = new mysqli("localhost", "root", "nJHttms84WLa", "sharepay");
        if ($mysqli->connect_error) {
            die("Connection failed: " . $mysqli->connect_error);
        }

        // Sum the amount_deposited in messages table
        $sql = "SELECT SUM(amount_deposited) AS total_amount FROM messages WHERE group_id = ?";
        $stmt = $mysqli->prepare($sql);
        $stmt->bind_param("i", $group_id);
        $stmt->execute();
        $result = $stmt->get_result();

        if ($result) {
            $row = $result->fetch_assoc();
            $totalAmountDeposited = floatval($row['total_amount']);
        } else {
            echo "Error fetching total amount deposited: " . $mysqli->error;
            exit;
        }

        // Get the target_fee from the groups table
        $sql = "SELECT target_fee FROM groups WHERE group_id = ?";
        $stmt = $mysqli->prepare($sql);
        $stmt->bind_param("i", $group_id);
        $stmt->execute();
        $result = $stmt->get_result();

        if ($result) {
            $row = $result->fetch_assoc();
            $targetFee = floatval($row['target_fee']);
        } else {
            echo "Error fetching target fee: " . $mysqli->error;
            exit;
        }

        // Calculate the percentage
        if ($targetFee > 0) {
            $percentage = ($totalAmountDeposited / $targetFee) * 100;
            echo $percentage;
        } else {
            echo "Error: Target fee must be greater than 0.";
        }

        $stmt->close();
        $mysqli->close();
    } else {
        echo "Invalid group ID.";
    }
} else {
    echo "Invalid request method.";
}
?>
