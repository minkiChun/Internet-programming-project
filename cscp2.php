<?php
	$str = $_POST["param1"];
	$res = exec("python '/var/www/html/cscp2.py' '$str'");
	echo $res;
?>