<?php
// Include necessary database connection and functions here

// Check if the request method is POST
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    // Get user_id and group_id from the request
    $user_id = isset($_POST['user_id']) ? $_POST['user_id'] : null;
    $group_id = isset($_POST['group_id']) ? $_POST['group_id'] : null;

    if ($user_id === null || $group_id === null) {
        echo "user_id and group_id are required.";
    } else {
        // Connect to your database (use your database connection code)
        $mysqli = new mysqli("localhost", "root", "nJHttms84WLa", "sharepay");

        if ($mysqli->connect_error) {
            die("Connection failed: " . $mysqli->connect_error);
        }

        // Calculate the balance
       $sql = "SELECT g.collection_share - SUM(m.amount_deposited) AS balance
                FROM group_members g
                LEFT JOIN messages m ON g.group_id = m.group_id AND g.user_id = m.user_id
                WHERE g.user_id = ? AND g.group_id = ?";

        $stmt = $mysqli->prepare($sql);
        if ($stmt) {
            $stmt->bind_param("ss", $user_id, $group_id);
            $stmt->execute();
            $stmt->bind_result($balance);
            $stmt->fetch();

            echo json_encode(array('balance' => $balance));

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
