import { Avatar, Button, Divider, Flex, Heading, Input, InputGroup, Text, VStack } from '@chakra-ui/react'
import React, { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import authServices from '../services/auth-services'
import userServices from '../services/user-services'

export const ProfilePage = () => {

    const [user, setUser] = useState()

    const [image, setImage] = useState()

    const navigate = useNavigate()

    useEffect(() => {
        if (localStorage.getItem('user') == null) {
            navigate('/')
        }
    }, [navigate])

    useEffect(() => {
        setUser(authServices.getCurrentUser())
    }, [])

    const onSubmit = (e) => {
        e.preventDefault()
        console.log(image)

        userServices.setProfilePictureById(user.user_id, image).catch(err => {
            console.error(err)
        })
    }

    return (
        <VStack justify={"center"} mb={10}>
            <Heading size={"lg"} textAlign={"center"}>Your profile page</Heading>
            <Divider width={"50%"} />
            {user ? <>
                <Avatar size={"xl"} src={`http://localhost:8080/api/users/profile/${user.user_id}`} />
                <Heading size={"md"}>{user.username}</Heading>
                <Heading size={"md"}>{user.email}</Heading>
                <Text>Roles: {authServices.getTokenDetails().roles[0].authority}</Text>
                <form onSubmit={onSubmit}>
                    <Flex flexDir={"column"} justify="center">
                        <InputGroup>
                            <Input onChange={(e) => setImage(e.target.files[0])} type="file" accept="image/png, image/gif, image/jpeg" />
                        </InputGroup>
                        <Button type="submit" colorScheme={"facebook"}>Upload profile picture</Button>
                    </Flex>
                </form>
            </>
                : ""};
        </VStack>
    )
}
