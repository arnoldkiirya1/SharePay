<?php
// Include database connection and configuration
include('db.php');

// Check if the HTTP request method is POST
if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    // Get the data from the request body
    $data = json_decode(file_get_contents("php://input"));

    // Extract user data
    $phone = $data->phone;
    $first_name = $data->first_name;
    $last_name = $data->last_name;
    $photo = $data->photo;

    // Perform SQL insert
    $query = "INSERT INTO users (phone, first_name, last_name, photo, date_reg) VALUES (?, ?, ?, ?, NOW())";
    $stmt = $conn->prepare($query);
    $stmt->bind_param("ssss", $phone, $first_name, $last_name, $photo);

    if ($stmt->execute()) {
        $response = array('message' => 'User created successfully');
        echo json_encode($response);
    } else {
        $response = array('error' => 'User creation failed');
        echo json_encode($response);
    }

    // Close database connection
    $stmt->close();
    $conn->close();
}
?>
