<?php
error_reporting(0);
include_once("dbconnect.php");
$cpid = $_POST['cpid'];
$cpname = $_POST['cpname'];
$cpemail = $_POST['cpemail'];
$url = $_POST['url'];

$sql = "UPDATE CP SET CPNAME='$cpname' WHERE CPID='$cpid'";
if ($conn->query($sql) === TRUE)
{
    echo "success";
}
else
    {
    echo "failed";
}

$conn->close();
?>