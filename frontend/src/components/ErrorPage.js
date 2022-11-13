import { Heading, Text } from "@chakra-ui/react";
import { useRouteError } from "react-router-dom";

export default function ErrorPage() {
    const error = useRouteError();
    console.error(error);

    return (
        <div >
            <Heading textAlign="center">Oops!</Heading>
            <Text textAlign="center">Sorry, an unexpected error has occurred.</Text>
            <Text textAlign="center">
                <i>{error.statusText || error.message}</i>
            </Text>
        </div>
    );
}