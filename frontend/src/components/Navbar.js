import { Avatar, Box, Button, Flex, Heading, HStack, Input, InputGroup, InputLeftElement, Menu, MenuButton, MenuDivider, MenuGroup, MenuItem, MenuList, Spacer } from '@chakra-ui/react'
import React, { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { FaDoorOpen, FaNewspaper, FaSearch } from 'react-icons/fa'
import { CiSettings } from "react-icons/ci"
import authServices from '../services/auth-services'

export const Navbar = () => {

    const [user, setUser] = useState()
    const navigate = useNavigate()

    useEffect(() => {
        setUser(authServices.getCurrentUser())

        if (authServices.isTokenExpired()) {
            authServices.logout()
            navigate('/')
            window.location.reload()
        }
    }, [])

    const handleSearch = (e) => {
        if (e.key === "Enter") {
            navigate(`/search?title=${e.target.value}`)
            window.location.reload()
        }
    }

    return (
        <>
            <Flex p={2} bgColor={"#2B2F30"} borderColor={"#42474B"} borderBottomWidth={2.5} mb={5} width={"100%"} boxShadow={"lg"}>
                <HStack align={"center"} gap={10} width={"100%"}>
                    <Box>
                        <Heading ml={2} size={"sm"} textTransform={"uppercase"} letterSpacing={2}>My Blog</Heading>
                    </Box>
                    <Box style={{ margin: 0 }}>
                        <Button onClick={() => navigate('/')} size={"sm"} variant={"link"}>Home page</Button>
                    </Box>
                    {user ?
                        <Box style={{ margin: 0 }}>
                            <Button onClick={() => navigate('/write')} size={"sm"} variant={"link"}>Write new post</Button>
                        </Box>
                        : ""}
                    <Box style={{ margin: 0 }}>
                        <InputGroup>
                            <InputLeftElement
                                pointerEvents='none'
                                children={<FaSearch color='gray.300' />}
                            />
                            <Input onKeyDown={handleSearch}
                                type='text' placeholder='Search by title' />
                        </InputGroup>
                    </Box>
                    <Spacer style={{ margin: 0 }} />
                    {user ?
                        <Box style={{ margin: 0 }}>
                            <Menu style={{ margin: 0 }}>
                                <MenuButton cursor={"pointer"} as={Avatar} src={`http://localhost:8080/api/users/profile/${user.user_id}`} >
                                </MenuButton>
                                <MenuList>
                                    <MenuGroup title={user.username + " - " + authServices.getTokenDetails().roles[0].authority}>
                                        <MenuItem icon={<FaNewspaper />}>My Posts</MenuItem>
                                        <MenuItem onClick={() => navigate(`/settings`)} icon={<CiSettings />}>Settings </MenuItem>
                                    </MenuGroup>
                                    <MenuDivider />
                                    <MenuGroup title='Account'>
                                        <MenuItem icon={<FaDoorOpen />} onClick={() => { authServices.logout(); window.location.reload() }}>Logout</MenuItem>
                                    </MenuGroup>
                                </MenuList>
                            </Menu>
                        </Box> :
                        <Box>
                            <Button onClick={() => navigate('/register')} size={"sm"} borderRadius={15} marginRight={5} bgColor={"cadetblue"} variant={"outline"}>Register</Button>
                            <Button onClick={() => navigate('/login')} size={"sm"} borderRadius={15} variant={"outline"}>Login</Button>
                        </Box>
                    }
                </HStack>
            </Flex >
        </>
    )
}
