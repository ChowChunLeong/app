<?php
error_reporting(0);
include_once 'dbconnect.php';
$email = $_POST['email'];
$password = sha1($_POST['password']);
$phone = $_POST['phone'];
$name = $_POST['name'];
$role = $_POST['role'];
if (strlen($email) > 0){
  $sqlinsert = "INSERT INTO USER (EMAIL,PASSWORD,PHONE,NAME,ROLE) VALUES ('$email','$password','$phone','$name','$role')";
    if ($conn->query($sqlinsert) === TRUE){
       echo "success";
    }else {
        echo "failed";
    }
}else{
    echo "nodata";
}

?>

