import Stack from 'react-bootstrap/Stack';

function Login(){
    return(
        <main className="m-1">
            <div className="text-3xl font-bold underline">This is the login page. Powered by Tailwind CSS</div>
            <Stack gap={3}>
                <div className="p-2">First item</div>
                <div className="p-2">Second item</div>
                <div className="p-2">Third item</div>
            </Stack>
        </main>
    )
}

export default Login;