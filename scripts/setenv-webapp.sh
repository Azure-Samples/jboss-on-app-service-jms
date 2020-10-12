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
