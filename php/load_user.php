<?php
error_reporting(0);
include_once("dbconnect.php");

$email = $_POST['email'];
$sql = "SELECT * FROM CP WHERE CPEMAIL= '$email'";
$result = $conn->query($sql);
if ($result->num_rows > 0) {
    $response["cps"] = array();
    while ($row = $result ->fetch_assoc()){
        $cplist = array();
        $cplist[cpid] = $row["CPID"];
        $cplist[cpname] = $row["CPNAME"];
        $cplist[cpemail] = $row["CPEMAIL"];
        array_push($response["cps"], $cplist);
    }
    echo json_encode($response);
}else{
    echo "nodata";
}
?>