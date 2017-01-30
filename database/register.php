<?php
 
include_once("DB_Functions.php");
$db = new DB_Functions();
 
// json response array
$response = array("error" => FALSE);
 
if (isset($_POST['username'])   && 
    isset($_POST['name'])       && 
    isset($_POST['email'])      && 
    isset($_POST['birthday'])   &&
    isset($_POST['gender'])     &&
    isset($_POST['password'])   &&
    isset($_POST['cpassword'])  &&
    isset($_POST['invitecode']) ) {
 
    // receiving the post params
    $username    = $_POST['username'];
    $name        = $_POST['name'];
    $email       = $_POST['email'];
    $birthday    = $_POST['birthday'];
    $gender      = $_POST['gender'];
    $password    = $_POST['password'];
    $cpassword   = $_POST['cpassword'];
    $invitecode  = $_POST['invitecode'];

    

    // check if user is already existed with the same email
    if ($db->isUserExisted($email)) {
        // user already existed
        $response["error"]      = TRUE;
        $response["error_msg"]  = "User already existed with " . $email;
        echo json_encode($response);
    } else {
        // create a new user
        $user = $db->storeUser($username, $name, $email, $birthday, $gender, $password, $cpassword, $invitecode);
        if ($user) {
            // user stored successfully
            $response["error"] = FALSE;
            $response["uid"]                = $user["unique_id"];
            $response["user"]["username"]   = $user["username"];
            $response["user"]["name"]       = $user["name"];
            $response["user"]["email"]      = $user["email"];
            $response["user"]["birthday"]   = $user["birthday"];
            $response["user"]["gender"]     = $user["gender"];
            $response["user"]["invitecode"] = $user["invitecode"];
            $response["user"]["created_at"] = $user["created_at"];
            $response["user"]["updated_at"] = $user["updated_at"];
            echo json_encode($response);
        } else {
            // user failed to store
            $response["error"]      = TRUE;
            $response["error_msg"]  = "Unknown error occurred in registration!";
            echo json_encode($response);
        }
    }
} else {
    $response["error"]      = TRUE;
    $response["error_msg"]  = "Required parameters (name, email or password) is missing!";
    echo json_encode($response);
}
?>