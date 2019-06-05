<?php
    $cpid = $_POST['cpid'];
    if(unlink("/storage/ssd2/268/9074268/public_html/conferencepaper/PDF/".$cpid.".pdf")){
        echo "Deleted";
    }else{
        echo "No Deleted";
    }
?>