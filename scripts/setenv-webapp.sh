
# Gets the publishing credentials from the web app, and sets a variable named "creds". 
# the credentials are used later to deploy code to the web app.
set_creds()
{
	response=`az webapp deployment list-publishing-credentials -g $1 --name $2`

	user=`echo $response \
		| jq '.publishingUserName' \
		| sed -e 's/"//g'`

	pass=`echo $response \
		| jq '.publishingPassword' \
		| sed -e 's/"//g'`

	export cred="$user:$pass"
}
