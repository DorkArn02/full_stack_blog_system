import { Badge, Button, Divider, Flex, Heading, Input, InputGroup, InputLeftElement, VStack } from '@chakra-ui/react'
import React, { useEffect } from 'react'
import { FaLock, FaUser } from 'react-icons/fa'
import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import authServices from '../services/auth-services'

export const Login = () => {

    const [data, setData] = useState()
    const [error, setError] = useState()
    const navigate = useNavigate()

    useEffect(() => {
        if (localStorage.getItem('user') != null) {
            navigate('/')
        }
    }, [navigate])

    const handleChange = (e) => {
        setData({ ...data, [e.target.name]: e.target.value })
        setError("")
    }

    const onSubmit = (e) => {
        e.preventDefault()
        if (data.username.length !== 0 && data.password.length !== 0) {
            authServices.login(data).catch(err => {
                if (err.response.status === 401 || err.response.status === 404) {
                    setError("Wrong credentials.")
                }
            })
        }

    }
    return (
        <>
            <VStack justify={"center"} mb={10}>
                <Heading size={"lg"} textAlign={"center"}>Login page</Heading>
                <Divider width={"50%"} />
            </VStack>
            <form onSubmit={onSubmit}>
                <Flex justify={"center"}>
                    <VStack boxShadow={"lg"} p={3} width="33%">
                        <InputGroup>
                            <InputLeftElement pointerEvents='none'
                                children={<FaUser color='gray.300' />} />
                            <Input isInvalid={error} required name="username" onChange={handleChange} type="text" placeholder={"Username"}></Input>
                        </InputGroup>
                        <InputGroup>
                            <InputLeftElement pointerEvents='none'
                                children={<FaLock color='gray.300' />} />
                            <Input isInvalid={error} required type="password" name="password" onChange={handleChange} placeholder={"Password"}></Input>
                        </InputGroup>
                        <Button variant={"solid"} type="submit">Login</Button>
                        {error ? <Badge variant={"solid"} bgColor={"red.400"}>{error}</Badge> : ""}
                    </VStack>
                </Flex>
            </form>
        </>
    )
}
