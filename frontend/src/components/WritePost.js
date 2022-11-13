import { Button, Divider, Flex, Heading, Input, InputGroup, Select, VStack } from '@chakra-ui/react'
import React, { useEffect, useMemo, useRef, useState } from 'react'
import JoditEditor from 'jodit-react';
import categoryService from "../services/category-services"
import postServices from '../services/post-services';
import { useNavigate } from 'react-router-dom';

export const WritePost = () => {

    const config = useMemo(
        () => ({
            readonly: false,
            placeholder: 'Start typings...',
            style: { color: 'black' }
        }),
        []
    );

    const editor = useRef(null);
    const [content, setContent] = useState('');
    const [title, setTitle] = useState('')
    const [categoryList, setCategoryList] = useState('')
    const [category, setCategory] = useState('')


    useEffect(() => {
        categoryService.getCategories().then(resp => setCategoryList(resp))
    }, [])

    const navigate = useNavigate()


    const onSubmit = (e) => {
        e.preventDefault()

        postServices.addNewPost(title, content).then(() => {
            navigate('/')
        })
    }

    return (
        <>
            <VStack justify={"center"} mb={10}>
                <Heading size={"lg"} textAlign={"center"}>Write post page</Heading>
                <Divider width={"50%"} />
            </VStack>
            <form onSubmit={onSubmit}>
                <Flex justify={"center"}>
                    <VStack width="33%">
                        <InputGroup>
                            <Input onChange={(e) => setTitle(e.target.value)} required name="title" type="text" placeholder='Post title' />
                        </InputGroup>
                        <InputGroup>
                            <Select value={category} onChange={(e) => setCategory(e.target.value)} required placeholder='Select a post category'>
                                {categoryList ? categoryList.map((item, i) => {
                                    return <option key={i}>{item.name}</option>
                                }) : ""}
                            </Select>
                        </InputGroup>

                        <JoditEditor
                            ref={editor}
                            value={content}
                            config={config}
                            tabIndex={1}
                            onBlur={newContent => setContent(newContent)}
                        //onChange={newContent => setContent(newContent)}
                        />
                        <Button type="submit">Publish new post</Button>
                    </VStack>
                </Flex>
            </form>
        </>
    )
}
