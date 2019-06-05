<?php
error_reporting(0);
include_once("dbconnect.php");
$name = $_POST['name'];
$phone = $_POST['phone'];
$email = $_POST['email'];
$sqlinsert = "update USER set NAME = '$name', PHONE = '$phone' WHERE EMAIL = '$email'";
if ($conn->query($sqlinsert) === TRUE){
    echo "success";
}else {
    echo "failed";
}
?>