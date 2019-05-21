<html>
<head>
</head>
<body>

<?php

	$extension = pathinfo($_FILES['picture']['name'], PATHINFO_EXTENSION);
	$file_identifier = pathinfo($_FILES['picture']['name'], PATHINFO_FILENAME);

	$input_path = '/var/www/html/asofterspaceen/qrimagewriter/converter/input_' . $file_identifier . '.' . $extension;

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

	$output_name = 'output_' . $file_identifier . '.png';
	
	rename("/var/www/html/asofterspaceen/qrimagewriter/converter/output.png", "/var/www/html/asofterspaceen/qrimagewriter/converter/" . $output_name);

	echo("Here is your picture:<br><br>");
	echo('<img src="https://www.asofterspace.com/qrimagewriter/converter/' . $output_name . '" />');
	
?>

</body>
