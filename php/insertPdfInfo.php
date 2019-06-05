<?php
error_reporting(0);
include_once("dbconnect.php");
$cpname = $_POST['cpname'];
$cpemail = $_POST['cpemail'];


 $sql = "INSERT INTO CP(CPNAME,CPEMAIL) VALUES ('$cpname','$cpemail')";
    if ($conn->query($sql) === TRUE){
       
        echo "success";
    }else {
        echo "failed";
    }
?>
