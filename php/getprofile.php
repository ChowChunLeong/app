<?php
error_reporting(0);
include_once("dbconnect.php");
$email = $_POST['email'];
$sql = "SELECT * FROM USER WHERE EMAIL = '$email'";
$result = $conn->query($sql);
if ($result->num_rows > 0) {
    $response["profile"] = array();
    while ($row = $result ->fetch_assoc()){
        $datalist= array();
        $datalist[name] = $row["NAME"];
        $datalist[phone] = $row["PHONE"];
        $datalist[email] = $row["EMAIL"];
        $datalist[password] = $row["PASSWORD"];
		$datalist[role] = $row["ROLE"];
        array_push($response["profile"], $datalist);
    }
    echo json_encode($response);
}else{
    echo "nodata";
}
?>