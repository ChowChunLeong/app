<?php
error_reporting(0);
include_once("dbconnect.php");
$cpid = $_POST['cpid'];
$sql = "DELETE FROM CP WHERE CPID = $cpid";
    if ($conn->query($sql) === TRUE){
        echo "success";
    }else {
        echo "failed";
    }

$conn->close();
?>