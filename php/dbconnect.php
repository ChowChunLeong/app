<?php
$servername = "localhost";
$username   = "id9074268_leong";
$password   = "535488967";
$dbname     = "id9074268_conference";

$conn = new mysqli($servername, $username, $password, $dbname);
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}
?>