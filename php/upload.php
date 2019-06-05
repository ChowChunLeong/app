<?php
error_reporting(0);
include_once 'dbconnect.php';
$sql = "SELECT MAX(CPID) AS item FROM CP";
$result = $conn-> query($sql);
$row = $result->fetch_assoc();
    
$file_name = $row['item'].".pdf"; //name of your file
$server_path = "/storage/ssd2/268/9074268/public_html/conferencepaper/PDF/"; //server path to folder
$web_path = "http://ccl000.000webhostapp.com/conferencepaper/PDF/"; //web path to folder

$file = $server_path.$file_name;
file_put_contents($file,"");

$fp = fopen("php://input", 'r');
while ($buffer =  fread($fp, 8192)) {
    file_put_contents($file,$buffer,FILE_APPEND | LOCK_EX);
}

echo $web_path.$file_name;
?>