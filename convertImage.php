<html>
<head>
</head>
<body>

<?php

	$extension = pathinfo($_FILES['picture']['name'], PATHINFO_EXTENSION);

	$input_path = '/var/www/html/asofterspaceen/qrimagewriter/converter/input.' . $extension;

	move_uploaded_file($_FILES['picture']['tmp_name'], $input_path);

	file_put_contents(
		"/var/www/html/asofterspaceen/qrimagewriter/converter/input.json",
		"{" .
		'"baseurl": "http://www.ideanding.com/etiquetadora/?id=",' .
		'"id": "lxxl213473",' .
		'"picture": "' . $input_path . '",' .
		'"logo": "logo.png",' .
		'"width": ' . $_POST['width'] . ',' .
		'"height": ' . $_POST['height'] .
		'}'
	);

	exec("/var/www/html/asofterspaceen/qrimagewriter/converter/run.sh");

?>

Here is your picture:<br><br>
<img src="https://www.asofterspace.com/qrimagewriter/converter/output.png" />

</body>
